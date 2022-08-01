import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IEventLog } from 'app/entities/event-log/event-log.model';

export interface IEventLogBook {
  id?: number;
  uuid?: string | null;
  createdDate?: dayjs.Dayjs | null;
  updatedDate?: dayjs.Dayjs | null;
  name?: string;
  description?: string;
  archieved?: boolean | null;
  user?: IUser | null;
  eventLogs?: IEventLog[] | null;
}

export class EventLogBook implements IEventLogBook {
  constructor(
    public id?: number,
    public uuid?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public updatedDate?: dayjs.Dayjs | null,
    public name?: string,
    public description?: string,
    public archieved?: boolean | null,
    public user?: IUser | null,
    public eventLogs?: IEventLog[] | null
  ) {
    this.archieved = this.archieved ?? false;
  }
}

export function getEventLogBookIdentifier(eventLogBook: IEventLogBook): number | undefined {
  return eventLogBook.id;
}
