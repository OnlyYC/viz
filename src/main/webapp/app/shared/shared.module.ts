import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { VizSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [VizSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [VizSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VizSharedModule {
  static forRoot() {
    return {
      ngModule: VizSharedModule
    };
  }
}
