using backend.DTOs;

namespace backend.Services
{
	public interface ICourierService
	{
		
			Task<IEnumerable<CourierDto>> GetAllCouriersAsync();
		
	}
}
