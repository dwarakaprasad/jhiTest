import dayjs from 'dayjs/esm';
import { EMPLOYERTYPE } from 'app/entities/enumerations/employertype.model';

export interface IEmployer {
  id?: number;
  name?: string;
  ein?: number;
  started?: dayjs.Dayjs | null;
  employerType?: EMPLOYERTYPE;
}

export class Employer implements IEmployer {
  constructor(
    public id?: number,
    public name?: string,
    public ein?: number,
    public started?: dayjs.Dayjs | null,
    public employerType?: EMPLOYERTYPE
  ) {}
}

export function getEmployerIdentifier(employer: IEmployer): number | undefined {
  return employer.id;
}
