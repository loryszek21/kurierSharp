using backend.Models;
using Microsoft.EntityFrameworkCore;

namespace backend.Data.Seed
{
	public static class PackageSeed
	{
		public static void Seed(ModelBuilder modelBuilder)
		{
			modelBuilder.Entity<Package>().HasData(
				new
				{
					Id = 1,
					TrackingNumber = "TRK123456",
					WeightKg = 2.5,
					Status = PackageStatus.Created,
					CreatedAt = new DateTime(2024, 4, 24, 12, 0, 0, DateTimeKind.Utc),
					DeliveredAt = (DateTime?)null,
					SenderId = 1,
					RecipientId = 2,
					CourierId = 3,
					AddressId = 1
				}
			);
		}
	}
}
