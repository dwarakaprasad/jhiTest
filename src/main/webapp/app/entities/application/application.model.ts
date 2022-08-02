import dayjs from 'dayjs/esm';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { APPLICATIONTYPE } from 'app/entities/enumerations/applicationtype.model';

export interface IApplication {
  id?: number;
  applicationType?: APPLICATIONTYPE;
  initiationdate?: dayjs.Dayjs;
  submissionDate?: dayjs.Dayjs;
  finalizationdate?: dayjs.Dayjs;
  applicationIdentifier?: string | null;
  applicants?: IApplicant[] | null;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public applicationType?: APPLICATIONTYPE,
    public initiationdate?: dayjs.Dayjs,
    public submissionDate?: dayjs.Dayjs,
    public finalizationdate?: dayjs.Dayjs,
    public applicationIdentifier?: string | null,
    public applicants?: IApplicant[] | null
  ) {}
}

export function getApplicationIdentifier(application: IApplication): number | undefined {
  return application.id;
}
