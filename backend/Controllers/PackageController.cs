using backend.DTOs;
using backend.Models;
using backend.Services;
using Humanizer;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;

namespace backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class PackageController : ControllerBase
	{
		private readonly IPackageService _packageService;

		public PackageController(IPackageService packageService)
		{
			_packageService = packageService;
		}

		[HttpGet]
		public async Task<ActionResult<IEnumerable<PackageDto>>> GetPackage()
		{
			var packages = await _packageService.GetAllPackagesAsync();
			return Ok(packages);

		}

		[HttpGet("{id}")]
		public async Task<ActionResult<Package>> GetPackageById(int id)
		{
			var package = await _packageService.GetPackageByIdAsync(id);
			if (package == null)
			{
				return NotFound();
			}

			return Ok(package);
		}
		[HttpGet("courier/{courierId}")]
		public async Task<ActionResult<IEnumerable<PackageDto>>> GetPackagesByCourier(int courierId)
		{
			var packages = await _packageService.GetPackagesByCourierIdAsync(courierId);
			return Ok(packages);
		}

		[HttpPut("modify-status")]
		public async Task<IActionResult> UpdatePackageStatus([FromBody] PackageStatusDto dto)
		{
			var result = await _packageService.UpdatePackageStatusAsync(dto);
			if (!result)
			{
				return NotFound($"Package with ID {dto.Id} not found");
			}
			return Ok("Update successfully");
		}
		[HttpPost]
		public async Task<ActionResult<Package>> CreatePackage([FromBody] CreatePackageDto dto)

		{
			var createdPackage = await _packageService.CreatePackageAsync(dto);
			return CreatedAtAction(nameof(GetPackageById), new
			{
				id = createdPackage.Id
			}, createdPackage);
		}
		[HttpPost("upload-photo")] // Endpoint: /api/Package/upload-photo
		public IActionResult UploadPhoto([FromBody] PhotoUploadRequestDto photoRequest)
		{
			if (photoRequest == null || string.IsNullOrEmpty(photoRequest.PackageId) || string.IsNullOrEmpty(photoRequest.Base64Image))
			{
				return BadRequest("Invalid photo upload request. PackageId and Base64Image are required.");
			}

			// Wypisz informację na konsolę serwera (lub użyj loggera)
			Console.WriteLine($"Received photo for Package ID: {photoRequest.PackageId}");
			Console.WriteLine($"Base64 Image Data Length: {photoRequest.Base64Image.Length}");
			// System.Diagnostics.Debug.WriteLine działa w trybie Debug
			Debug.WriteLine($"DEBUG: Received photo for Package ID: {photoRequest.PackageId}, Image Length: {photoRequest.Base64Image.Length}");


			// TODO: W przyszłości tutaj będzie logika przetwarzania obrazu:
			// 1. Zdekoduj Base64Image do tablicy bajtów (byte[]).
			//    byte[] imageBytes = Convert.FromBase64String(photoRequest.Base64Image);
			// 2. Zapisz obraz na serwerze (np. do pliku, do bazy danych, do Azure Blob Storage itp.).
			// 3. Powiąż zapisany obraz z paczką o ID = photoRequest.PackageId w bazie danych.

			// Na razie zwracamy prostą odpowiedź
			return Ok(new { status = "success", message = $"Photo for package {photoRequest.PackageId} received by server." });
		}
	
}
}
