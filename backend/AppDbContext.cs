namespace backend
{
	using backend.Data.Seed;
	using backend.Models;
	using Microsoft.EntityFrameworkCore;

	public class AppDbContext : DbContext
	{
		public DbSet<User> Users { get; set; }
		public DbSet<Address> Addresses { get; set; }
		public DbSet<Package> Packages { get; set; }

		public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

		protected override void OnModelCreating(ModelBuilder modelBuilder)
		{
			base.OnModelCreating(modelBuilder);

			modelBuilder.Entity<User>()
			   .HasOne(u => u.Address)
			   .WithMany()  // Address can be associated with many users
			   .HasForeignKey("AddressId")
			   .IsRequired()
			   .OnDelete(DeleteBehavior.Cascade);  // Optional:  Cascade delete address if user is deleted

			//Package - Address (One-to-One)
			modelBuilder.Entity<Package>()
			   .HasOne(p => p.Address)
			   .WithMany() // Address can be associated with many packages
			   .HasForeignKey("AddressId")
			   .IsRequired()
			   .OnDelete(DeleteBehavior.Cascade); // Optional: Cascade delete address if package is deleted

			// Package - Sender (One-to-Many)
			modelBuilder.Entity<Package>()
				.HasOne(p => p.Sender)
				.WithMany() // A User can send many packages
				.HasForeignKey(p => p.SenderId)
				.IsRequired()
				.OnDelete(DeleteBehavior.NoAction); // Prevent cascade delete (sender deletion would delete packages)

			// Package - Recipient (One-to-Many)
			modelBuilder.Entity<Package>()
				.HasOne(p => p.Recipient)
				.WithMany() // A User can receive many packages
				.HasForeignKey(p => p.RecipientId)
				.IsRequired()
				.OnDelete(DeleteBehavior.NoAction); // Prevent cascade delete (recipient deletion would delete packages)

			// Package - Courier (One-to-Many) (Optional Courier)
			modelBuilder.Entity<Package>()
				.HasOne(p => p.Courier)
				.WithMany() // A User can deliver many packages
				.HasForeignKey(p => p.CourierId)
				.IsRequired(false) // Courier is optional
				.OnDelete(DeleteBehavior.SetNull); 
		// // Seeding
		// AddressSeed.Seed(modelBuilder);
		// UserSeed.Seed(modelBuilder);
		// PackageSeed.Seed(modelBuilder);
	}
	}
}
