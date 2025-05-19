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
			return await _context.Packages.ToListAsync();
		}
	}
}
