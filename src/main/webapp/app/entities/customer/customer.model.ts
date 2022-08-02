import dayjs from 'dayjs/esm';
import { IPassport } from 'app/entities/passport/passport.model';
import { IPaymentInfo } from 'app/entities/payment-info/payment-info.model';
import { IAddress } from 'app/entities/address/address.model';
import { GENDER } from 'app/entities/enumerations/gender.model';

export interface ICustomer {
  id?: number;
  firstName?: string;
  middleName?: string | null;
  lastName?: string;
  dob?: dayjs.Dayjs;
  gender?: GENDER;
  passport?: IPassport | null;
  paymentInfos?: IPaymentInfo[] | null;
  addresses?: IAddress[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string,
    public middleName?: string | null,
    public lastName?: string,
    public dob?: dayjs.Dayjs,
    public gender?: GENDER,
    public passport?: IPassport | null,
    public paymentInfos?: IPaymentInfo[] | null,
    public addresses?: IAddress[] | null
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
