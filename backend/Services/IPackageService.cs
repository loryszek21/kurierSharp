using backend.DTOs;
using backend.Models;

namespace backend.Services
{
	public interface IPackageService
	{
		Task<IEnumerable<PackageDto>> GetAllPackagesAsync();
		Task<Package> GetPackageByIdAsync(int packageId);
		Task<IEnumerable<PackageDto>> GetPackagesByCourierIdAsync(int courierId);
		Task<bool> UpdatePackageStatusAsync(PackageStatusDto package);
		Task<Package> CreatePackageAsync(CreatePackageDto package);
	}
}
