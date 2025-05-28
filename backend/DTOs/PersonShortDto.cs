namespace backend.DTOs
{
	public class PersonShortDto
	{
		public int Id { get; set; }
		public string Name { get; set; } = null!;
		public string Surname { get; set; } = null!;
		public string PhoneNumber { get; set; } = null!;
	}
}
