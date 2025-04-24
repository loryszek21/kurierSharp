using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace backend.Migrations
{
    /// <inheritdoc />
    public partial class AddInitData : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.InsertData(
                table: "Users",
                columns: new[] { "Id", "Address", "City", "Country", "Email", "Name", "PhoneNumber", "PostalCode", "Region" },
                values: new object[,]
                {
                    { 1, "ul. Kwiatowa 5", "Warszawa", "Polska", "jan@example.com", "Jan Kowalski", "123456789", "00-001", "Mazowieckie" },
                    { 2, "ul. Różana 7", "Kraków", "Polska", "anna@example.com", "Anna Nowak", "987654321", "30-002", "Małopolskie" }
                });

            migrationBuilder.InsertData(
                table: "Packages",
                columns: new[] { "Id", "CourierId", "CreatedAt", "DeliveredAt", "RecipientId", "SenderId", "Status", "TrackingNumber", "WeightKg" },
                values: new object[] { 1, null, new DateTime(2024, 4, 24, 12, 0, 0, 0, DateTimeKind.Utc), null, 2, 1, 0, "TRK123456", 2.5 });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "Packages",
                keyColumn: "Id",
                keyValue: 1);

            migrationBuilder.DeleteData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 1);

            migrationBuilder.DeleteData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 2);
        }
    }
}
