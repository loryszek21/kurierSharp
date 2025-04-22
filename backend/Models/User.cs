namespace backend.Models
{
	public class User
	{
		public int Id { get; set; }
		public string Name { get; set; }
		public string Email { get; set; }
		public string PhoneNumber { get; set; }
		public string Address { get; set; }
		public string City { get; set; }
		public string Region { get; set; }
		public string PostalCode { get; set; }
		public string Country { get; set; }

		public User(int id, string name, string email, string phoneNumber, string address, string city, string region, string postalCode, string country)
		{
			Id = id;
			Name = name;
			Email = email;
			PhoneNumber = phoneNumber;
			Address = address;
			City = city;
			Region = region;
			PostalCode = postalCode;
			Country = country;
		}
	}

}
