using backend.DTOs;
using backend.Models;
using Microsoft.EntityFrameworkCore;
using System.IO;

namespace backend.Services
{
	public class PackageService : IPackageService
	{
		private readonly AppDbContext _context;

		public PackageService(AppDbContext context)
		{
			_context = context;
		}

		public async Task<IEnumerable<PackageDto>> GetAllPackagesAsync()
		{
			//return await _context.Packages.ToListAsync();
			return await _context.Packages
				   .Include(p => p.Sender).ThenInclude(s => s.Address)
				   .Include(p => p.Recipient).ThenInclude(r => r.Address)
				   .Include(p => p.Courier).ThenInclude(c => c.Address)
				   .Include(p => p.Address)
				   .Select(p => new PackageDto
				   {
					   Id = p.Id,
					   TrackingNumber = p.TrackingNumber,
					   WeightKg = p.WeightKg,
					   Status = p.Status,
					   CreatedAt = p.CreatedAt,
					   DeliveredAt = p.DeliveredAt,
					   Address = new AddressDto
					   {
						   Id = p.Address.Id,
						   Street = p.Address.Street,
						   City = p.Address.City,
						   Region = p.Address.Region,
						   PostalCode = p.Address.PostalCode,
						   Country = p.Address.Country
					   },
					   SenderId = p.SenderId,
					   Sender = new PersonShortDto
					   {
						   Id = p.Sender.Id,
						   Name = p.Sender.Name,
						   Surname = p.Sender.Surname,
						   PhoneNumber = p.Sender.PhoneNumber
					   },
					   RecipientId = p.RecipientId,
					   Recipient = new PersonShortDto
					   {
						   Id = p.Recipient.Id,
						   Name = p.Recipient.Name,
						   Surname = p.Recipient.Surname,
						   PhoneNumber = p.Recipient.PhoneNumber
					   }
				   })
				   .ToListAsync();
		}

		public async Task<Package?> GetPackageByIdAsync(int packageId)
		{
			return await _context.Packages
				.Include(p => p.Sender)
				.Include(p => p.Recipient)
				.Include(p => p.Courier)
				.Include(p => p.Address)
				.FirstOrDefaultAsync(p => p.Id == packageId);
		}

		public async Task<IEnumerable<PackageDto>> GetPackagesByCourierIdAsync(int courierId)
		{
			return await _context.Packages
				.Where(p => p.CourierId == courierId)
				.Include(p => p.Sender).ThenInclude(s => s.Address)
				.Include(p => p.Recipient).ThenInclude(r => r.Address)
				.Include(p => p.Courier).ThenInclude(c => c.Address)
				.Include(p => p.Address)
				.Select(p => new PackageDto
				{
					Id = p.Id,
					TrackingNumber = p.TrackingNumber,
					WeightKg = p.WeightKg,
					Status = p.Status,
					CreatedAt = p.CreatedAt,
					DeliveredAt = p.DeliveredAt,
					Address = new AddressDto
					{
						Id = p.Address.Id,
						Street = p.Address.Street,
						City = p.Address.City,
						Region = p.Address.Region,
						PostalCode = p.Address.PostalCode,
						Country = p.Address.Country
					},
					SenderId = p.SenderId,
					Sender = new PersonShortDto
					{
						Id = p.Sender.Id,
						Name = p.Sender.Name,
						Surname = p.Sender.Surname,
						PhoneNumber = p.Sender.PhoneNumber
					},
					RecipientId = p.RecipientId,
					Recipient = new PersonShortDto
					{
						Id = p.Recipient.Id,
						Name = p.Recipient.Name,
						Surname = p.Recipient.Surname,
						PhoneNumber = p.Recipient.PhoneNumber
					}
				})
				   .ToListAsync();
		}
		public async Task<bool> UpdatePackageStatusAsync(PackageStatusDto package)
		{
			var packageFromDb = await _context.Packages.FindAsync(package.Id);
			if (packageFromDb == null)
			{
				return false;
			}
			packageFromDb.Status = package.NewStatus;
			await _context.SaveChangesAsync();
			return true;
		}
		public async Task<Package> CreatePackageAsync(CreatePackageDto dto)
		{
			var trackingNumber = Guid.NewGuid().ToString().Substring(0, 10).ToUpper();
			var address = new Address
			{
				Street = dto.Address.Street,
				City = dto.Address.City,
				Region = dto.Address.Region,
				PostalCode = dto.Address.PostalCode,
				Country = dto.Address.Country,
			};
			_context.Addresses.Add(address);
			var package = new Package
			{
				TrackingNumber = trackingNumber,
				WeightKg = dto.WeightKg,
				Address = address,
				SenderId = dto.SenderId,
				RecipientId = dto.RecipientId,
				CourierId = dto.CourierId,
				Status = PackageStatus.Created
			};
			_context.Packages.Add(package);
			await _context.SaveChangesAsync();

			return package;
		}
	}
}
