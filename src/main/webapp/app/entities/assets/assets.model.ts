import { IAddress } from 'app/entities/address/address.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ASSETTYPE } from 'app/entities/enumerations/assettype.model';

export interface IAssets {
  id?: number;
  name?: string;
  assetType?: ASSETTYPE;
  address?: IAddress | null;
  applicants?: IApplicant[] | null;
}

export class Assets implements IAssets {
  constructor(
    public id?: number,
    public name?: string,
    public assetType?: ASSETTYPE,
    public address?: IAddress | null,
    public applicants?: IApplicant[] | null
  ) {}
}

export function getAssetsIdentifier(assets: IAssets): number | undefined {
  return assets.id;
}
