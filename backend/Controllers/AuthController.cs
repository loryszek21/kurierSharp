using backend.DTOs;
using backend.Services;
using Microsoft.AspNetCore.Mvc;

namespace backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class AuthController : ControllerBase
	{
		private readonly IUserService _userService;

		public AuthController(IUserService userService)
		{
			_userService = userService;
		}

		[HttpPost("login")] 
		public async Task<IActionResult> Login([FromBody] LoginRequestDto loginRequest)
		{
			if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Email) || string.IsNullOrEmpty(loginRequest.Password))
			{
				return BadRequest(new LoginResponseDto { Success = false, Message = "Email and password are required." });
			}

			var user = await _userService.AuthenticateAsync(loginRequest.Email, loginRequest.Password);

			if (user == null)
			{
				return Unauthorized(new LoginResponseDto { Success = false, Message = "Invalid email or password." });
			}

			// Logowanie pomyślne
			return Ok(new LoginResponseDto
			{
				Success = true,
				Message = "Login successful",
				UserId = user.Id,
				UserEmail = user.Email,
				UserName = user.Name // Możesz dodać Surname, jeśli chcesz
			});
		}
	}

}
