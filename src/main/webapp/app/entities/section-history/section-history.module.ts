import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebinarappSharedModule } from 'app/shared';
import {
    SectionHistoryComponent,
    SectionHistoryDetailComponent,
    SectionHistoryUpdateComponent,
    SectionHistoryDeletePopupComponent,
    SectionHistoryDeleteDialogComponent,
    sectionHistoryRoute,
    sectionHistoryPopupRoute
} from './';

const ENTITY_STATES = [...sectionHistoryRoute, ...sectionHistoryPopupRoute];

@NgModule({
    imports: [WebinarappSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SectionHistoryComponent,
        SectionHistoryDetailComponent,
        SectionHistoryUpdateComponent,
        SectionHistoryDeleteDialogComponent,
        SectionHistoryDeletePopupComponent
    ],
    entryComponents: [
        SectionHistoryComponent,
        SectionHistoryUpdateComponent,
        SectionHistoryDeleteDialogComponent,
        SectionHistoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebinarappSectionHistoryModule {}
