namespace backend.Models
{
	public class Package
	{
		public int Id { get; set; }
		public string TrackingNumber { get; set; }
		public double WeightKg { get; set; }
		public PackageStatus Status { get; set; } = PackageStatus.Created;
		public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
		public DateTime? DeliveredAt { get; set; }


		public int SenderId { get; set; }
		public User Sender { get; set; } = null!;

		public int RecipientId { get; set; }
		public User Recipient { get; set; } = null!;

		public int? CourierId { get; set; }
		public User? Courier { get; set; }

		public Package()
		{
		}

		public Package(int id, string trackingNumber, double weightKg, PackageStatus status, DateTime createdAt, DateTime? deliveredAt, int senderId, User sender, int recipientId, User recipient, int? courierId, User? courier)
		{
			Id = id;
			TrackingNumber = trackingNumber;
			WeightKg = weightKg;
			Status = status;
			CreatedAt = createdAt;
			DeliveredAt = deliveredAt;
			SenderId = senderId;
			Sender = sender;
			RecipientId = recipientId;
			Recipient = recipient;
			CourierId = courierId;
			Courier = courier;
		}
	}
}

