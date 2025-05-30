using backend.Models;

namespace backend.DTOs
{
	public class PackageStatusDto
	{
		public int Id { get; set; }
		public PackageStatus NewStatus { get; set; }
	}
}
