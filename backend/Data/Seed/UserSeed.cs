using backend.Models;
using Microsoft.EntityFrameworkCore;

namespace backend.Data.Seed
{
	public static class UserSeed
	{
		public static void Seed(ModelBuilder modelBuilder)
		{
			modelBuilder.Entity<User>().HasData(
				new
				{
					Id = 1,
					Name = "Jan",
					Surname = "Kowalski",
					Email = "jan@example.com",
					PhoneNumber = "123456789",
					AddressId = 1
				},
				new
				{
					Id = 2,
					Name = "Anna",
					Surname = "Nowak",
					Email = "anna@example.com",
					PhoneNumber = "987654321",
					AddressId = 2
				},
				new
				{
					Id = 3,
					Name = "Paweł",
					Surname = "Wiśniewski",
					Email = "pawel@example.com",
					PhoneNumber = "555123456",
					AddressId = 1
				}
			);
		}
	}
}
