using backend.DTOs;
using backend.Models;

namespace backend.Services
{
	public interface IUserService
	{
		Task<User> AuthenticateAsync(string email, string password);

	}
}
