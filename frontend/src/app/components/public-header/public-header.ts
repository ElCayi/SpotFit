import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-public-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './public-header.html',
  styleUrl: './public-header.css',
})
export class PublicHeaderComponent {
  @Input() activeSection: 'inicio' | 'servicios' = 'inicio';
  @Input() variant: 'glass' | 'solid' = 'glass';
}
