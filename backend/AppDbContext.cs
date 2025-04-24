namespace backend
{
	using backend.Models;
	using Microsoft.EntityFrameworkCore;
	using System;

	public class AppDbContext : DbContext
	{
		public DbSet<User> Users { get; set; } 
		public DbSet<Package> Packages { get; set; } 


		public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

		protected override void OnModelCreating(ModelBuilder modelBuilder)
		{
			base.OnModelCreating(modelBuilder);
			// Możesz tu konfigurować encje
			modelBuilder.Entity<User>().HasData(
	new User
	{
		Id = 1,
		Name = "Jan Kowalski",
		Email = "jan@example.com",
		PhoneNumber = "123456789",
		Address = "ul. Kwiatowa 5",
		City = "Warszawa",
		Region = "Mazowieckie",
		PostalCode = "00-001",
		Country = "Polska"
	},
	new User
	{
		Id = 2,
		Name = "Anna Nowak",
		Email = "anna@example.com",
		PhoneNumber = "987654321",
		Address = "ul. Różana 7",
		City = "Kraków",
		Region = "Małopolskie",
		PostalCode = "30-002",
		Country = "Polska"
	}
);

			modelBuilder.Entity<Package>().HasData(
				new Package
				{
					Id = 1,
					TrackingNumber = "TRK123456",
					WeightKg = 2.5,
					Status = PackageStatus.Created,
					CreatedAt = new DateTime(2024, 4, 24, 12, 0, 0, DateTimeKind.Utc),
					SenderId = 1,
					RecipientId = 2,
					CourierId = null
				}
			);
		}
	}
}



