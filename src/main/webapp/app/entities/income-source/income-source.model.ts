import { IEmployer } from 'app/entities/employer/employer.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { INCOMETYPE } from 'app/entities/enumerations/incometype.model';

export interface IIncomeSource {
  id?: number;
  incomeType?: INCOMETYPE;
  incomeAmount?: number;
  duration?: number;
  employer?: IEmployer | null;
  applicant?: IApplicant | null;
}

export class IncomeSource implements IIncomeSource {
  constructor(
    public id?: number,
    public incomeType?: INCOMETYPE,
    public incomeAmount?: number,
    public duration?: number,
    public employer?: IEmployer | null,
    public applicant?: IApplicant | null
  ) {}
}

export function getIncomeSourceIdentifier(incomeSource: IIncomeSource): number | undefined {
  return incomeSource.id;
}
