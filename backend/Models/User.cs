namespace backend.Models
{
	public class User
	{
		public int Id { get; set; }
		public string Name { get; set; }
		public string Surname { get; set; }
		public string Email { get; set; }
		public string PhoneNumber { get; set; }
		public Address Address { get; set; }

		public User()
		{
		}

		public User(int id, string name, string surname, string email, string phoneNumber, Address address)
		{
			Id = id;
			Name = name;
			Surname = surname;
			Email = email;
			PhoneNumber = phoneNumber;
			Address = address;
		}
	}
}

