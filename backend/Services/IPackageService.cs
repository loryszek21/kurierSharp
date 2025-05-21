using backend.Models;

namespace backend.Services
{
	public interface IPackageService
	{
		Task<IEnumerable<Package>> GetAllPackagesAsync();
		Task<Package> GetPackageByIdAsync(int packageId);
	}
}
