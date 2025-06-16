// np. w backend.Controllers/CourierController.cs
using backend.DTOs;
using backend.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace backend.Controllers
{
	[Route("api/[controller]")] // Będzie api/Courier
	[ApiController]
	public class CourierController : ControllerBase
	{
		private readonly ICourierService _courierService;

		public CourierController(ICourierService courierService)
		{
			_courierService = courierService;
		}

		[HttpGet] // GET api/Courier
		public async Task<ActionResult<IEnumerable<CourierDto>>> GetAllCouriers()
		{
			var couriers = await _courierService.GetAllCouriersAsync();
			if (couriers == null || !couriers.Any())
			{
				return NotFound("No couriers found.");
			}
			return Ok(couriers);
		}
	}
}