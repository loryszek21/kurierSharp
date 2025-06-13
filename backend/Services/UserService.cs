using backend.Models;
using Microsoft.EntityFrameworkCore;

namespace backend.Services
{

		public class UserService : IUserService
		{
			private readonly AppDbContext _context; // Twój DbContext

			public UserService(AppDbContext context)
			{
				_context = context;
			}

			public async Task<User> AuthenticateAsync(string email, string password)
			{
				if (string.IsNullOrEmpty(email) || string.IsNullOrEmpty(password))
					return null;

				var user = await _context.Users.SingleOrDefaultAsync(u => u.Email == email);

				// Sprawdź, czy użytkownik istnieje
				if (user == null)
					return null;

				if (user.Password != password) 
				{
					return null;
				}

				

				return user;
			}
		}
}
