import dayjs from 'dayjs/esm';
import { IIncomeSource } from 'app/entities/income-source/income-source.model';
import { IAssets } from 'app/entities/assets/assets.model';
import { IApplication } from 'app/entities/application/application.model';
import { GENDER } from 'app/entities/enumerations/gender.model';

export interface IApplicant {
  id?: number;
  firstName?: string;
  middleName?: string | null;
  lastName?: string;
  dob?: dayjs.Dayjs;
  gender?: GENDER;
  incomeSources?: IIncomeSource[] | null;
  assets?: IAssets[] | null;
  application?: IApplication | null;
}

export class Applicant implements IApplicant {
  constructor(
    public id?: number,
    public firstName?: string,
    public middleName?: string | null,
    public lastName?: string,
    public dob?: dayjs.Dayjs,
    public gender?: GENDER,
    public incomeSources?: IIncomeSource[] | null,
    public assets?: IAssets[] | null,
    public application?: IApplication | null
  ) {}
}

export function getApplicantIdentifier(applicant: IApplicant): number | undefined {
  return applicant.id;
}
