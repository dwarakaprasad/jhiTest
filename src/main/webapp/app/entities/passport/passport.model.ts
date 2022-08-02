import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { PASSPORTTYPE } from 'app/entities/enumerations/passporttype.model';

export interface IPassport {
  id?: number;
  identity?: string;
  expiry?: dayjs.Dayjs;
  issuingCountry?: string;
  documentNumber?: string | null;
  passportType?: PASSPORTTYPE;
  customer?: ICustomer | null;
}

export class Passport implements IPassport {
  constructor(
    public id?: number,
    public identity?: string,
    public expiry?: dayjs.Dayjs,
    public issuingCountry?: string,
    public documentNumber?: string | null,
    public passportType?: PASSPORTTYPE,
    public customer?: ICustomer | null
  ) {}
}

export function getPassportIdentifier(passport: IPassport): number | undefined {
  return passport.id;
}
