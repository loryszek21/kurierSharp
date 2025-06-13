namespace backend.DTOs
{
	public class LoginResponseDto
	{
		public bool Success { get; set; }
		public string Message { get; set; }
		public string UserEmail { get; set; } 
		public string UserName { get; set; }
		public int UserId { get; set; }
	}
}
