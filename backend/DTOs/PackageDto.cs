using backend.Models;

namespace backend.DTOs
{
	public class PackageDto
	{
		public int Id { get; set; }
		public string TrackingNumber { get; set; }
		public double? WeightKg { get; set; }
		public PackageStatus Status { get; set; }
		public DateTime CreatedAt { get; set; }
		public DateTime? DeliveredAt { get; set; }
		public AddressDto Address { get; set; } = null!;
		public int SenderId { get; set; }
		public PersonShortDto Sender { get; set; } = null!;
		public int RecipientId { get; set; }
		public PersonShortDto Recipient { get; set; } = null!;
		public PersonShortDto Courier { get; set; } = null!;
		}


}

