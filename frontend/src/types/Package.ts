import type { Address } from './Address';
import type { Person } from './Person';
	type PackageStatus =
    | "Created"
    | "PickedUp"
    | "InTransit"
    | "Delivered"
    | "Returned";
export interface Package {
  id: number;
  trackingNumber: string;
  weightKg?: number | null;
  status: PackageStatus;
  createdAt: string; 
  deliveredAt?: string | null;
  address: Address;
  senderId: number;
  sender: Person;
  recipientId: number;
  recipient: Person;
  courier: Person | null;
}