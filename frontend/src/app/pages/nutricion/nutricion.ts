import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-nutricion',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './nutricion.html',
  styleUrls: ['./nutricion.css']
})
export class NutricionComponent {}
