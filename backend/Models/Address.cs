namespace backend.Models
{
	public class Address
	{
		public int Id { get; set; }
		public string? Street { get; set; }
		public string? City { get; set; }
		public string? Region { get; set; }
		public string? PostalCode { get; set; }
		public string? Country { get; set; }

		public Address()
		{
		}

		public Address(int id, string? street, string? city, string? region, string? postalCode, string? country)
		{
			Id = id;
			Street = street;
			City = city;
			Region = region;
			PostalCode = postalCode;
			Country = country;
		}
	}

}
