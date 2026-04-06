import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PublicHeaderComponent } from '../../components/public-header/public-header';
import { PublicFooterComponent } from '../../components/public-footer/public-footer';

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [RouterModule, PublicHeaderComponent, PublicFooterComponent],
  templateUrl: './index.html',
  styleUrl: './index.css',
})
export class IndexComponent {

}
