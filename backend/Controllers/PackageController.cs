using backend.Models;
using backend.Services;
using Microsoft.AspNetCore.Mvc;

namespace backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class PackageController : ControllerBase
	{
		private readonly IPackageService _packageService;

		public PackageController(IPackageService packageService)
		{
			_packageService = packageService;
		}

		[HttpGet]
		public async Task<ActionResult<IEnumerable<Package>>> GetPackage()
		{
			var packages = await _packageService.GetAllPackagesAsync();
			return Ok(packages);

		}

		[HttpGet("{id}")]
		public async Task<ActionResult<Package>> GetPackageById(int id)
		{
			var package = await _packageService.GetPackageByIdAsync(id);
			if (package == null)
			{
				return NotFound();
			}

			return Ok(package);
		}
	}
	}
