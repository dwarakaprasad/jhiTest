import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssets } from '../assets.model';

@Component({
  selector: 'jhi-assets-detail',
  templateUrl: './assets-detail.component.html',
})
export class AssetsDetailComponent implements OnInit {
  assets: IAssets | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assets }) => {
      this.assets = assets;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
