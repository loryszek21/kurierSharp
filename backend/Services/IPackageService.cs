using backend.Models;

namespace backend.Services
{
	public interface IPackageService
	{
		Task<IEnumerable<Package>> GetAllPackagesAsync();
	}
}
