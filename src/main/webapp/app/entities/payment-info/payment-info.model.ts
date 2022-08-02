import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { PAYMENTTYPE } from 'app/entities/enumerations/paymenttype.model';

export interface IPaymentInfo {
  id?: number;
  paymentType?: PAYMENTTYPE;
  number?: number;
  expiry?: dayjs.Dayjs | null;
  security?: number | null;
  customer?: ICustomer | null;
}

export class PaymentInfo implements IPaymentInfo {
  constructor(
    public id?: number,
    public paymentType?: PAYMENTTYPE,
    public number?: number,
    public expiry?: dayjs.Dayjs | null,
    public security?: number | null,
    public customer?: ICustomer | null
  ) {}
}

export function getPaymentInfoIdentifier(paymentInfo: IPaymentInfo): number | undefined {
  return paymentInfo.id;
}
