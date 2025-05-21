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

		public async Task<IEnumerable<Package>> GetAllPackagesAsync()
		{
			//return await _context.Packages.ToListAsync();
			return await _context.Packages
	.Include(p => p.Sender)
	.Include(p => p.Recipient)
	.Include(p => p.Courier)
	.Include(p => p.Address)
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

	}
}
