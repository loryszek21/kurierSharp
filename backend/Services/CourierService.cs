using backend.DTOs;
using Microsoft.EntityFrameworkCore;

namespace backend.Services
{
	public class CourierService : ICourierService
	{
		private readonly AppDbContext _context;
		public CourierService(AppDbContext context)
		{
			_context = context;
		}

		public async Task<IEnumerable<CourierDto>> GetAllCouriersAsync()
		{
			// Na razie pobieramy wszystkich użytkowników jako potencjalnych kurierów
			// W przyszłości możesz dodać filtrowanie po roli, itp.
			var users = await _context.Users

									  .ToListAsync();

			return users.Select(user => new CourierDto
			{
				Id = user.Id,
				Name = user.Name,
				Surname = user.Surname,
				PhoneNumber = user.PhoneNumber


			});

		}
	}
}
