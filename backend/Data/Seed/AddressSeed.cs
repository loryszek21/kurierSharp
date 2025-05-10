using backend.Models;
using Microsoft.EntityFrameworkCore;

namespace backend.Data.Seed
{
	public static class AddressSeed
	{
		public static void Seed(ModelBuilder modelBuilder)
		{
			modelBuilder.Entity<Address>().HasData(
				new Address
				{
					Id = 1,
					Street = "ul. Kwiatowa 5",
					City = "Warszawa",
					Region = "Mazowieckie",
					PostalCode = "00-001",
					Country = "Polska"
				},
				new Address
				{
					Id = 2,
					Street = "ul. Różana 7",
					City = "Kraków",
					Region = "Małopolskie",
					PostalCode = "30-002",
					Country = "Polska"
				},
				new Address
				{
					Id = 3,
					Street = "ul. Słoneczna 10",
					City = "Poznań",
					Region = "Wielkopolskie",
					PostalCode = "60-003",
					Country = "Polska"
				}
			);
		}
	}
}
