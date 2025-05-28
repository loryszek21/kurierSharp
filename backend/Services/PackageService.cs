using backend.DTOs;
using backend.Models;
using Microsoft.EntityFrameworkCore;

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
	}
}
