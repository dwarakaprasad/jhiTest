import { ICustomer } from 'app/entities/customer/customer.model';

export interface IAddress {
  id?: number;
  street1?: string;
  street2?: string | null;
  city?: string;
  state?: string | null;
  zipCode?: number;
  customer?: ICustomer | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public street1?: string,
    public street2?: string | null,
    public city?: string,
    public state?: string | null,
    public zipCode?: number,
    public customer?: ICustomer | null
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
