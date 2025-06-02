namespace backend.DTOs
{
	public class CreatePackageDto
	{
		public double? WeightKg { get; set; }
		public AddressDto Address { get; set; }

		public int SenderId { get; set; }
		public int RecipientId { get; set; }
		public int? CourierId { get; set; }
	}
}
