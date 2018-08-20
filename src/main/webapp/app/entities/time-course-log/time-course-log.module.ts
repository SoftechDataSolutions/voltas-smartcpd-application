import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebinarappSharedModule } from 'app/shared';
import {
    TimeCourseLogComponent,
    TimeCourseLogDetailComponent,
    TimeCourseLogUpdateComponent,
    TimeCourseLogDeletePopupComponent,
    TimeCourseLogDeleteDialogComponent,
    timeCourseLogRoute,
    timeCourseLogPopupRoute
} from './';

const ENTITY_STATES = [...timeCourseLogRoute, ...timeCourseLogPopupRoute];

@NgModule({
    imports: [WebinarappSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TimeCourseLogComponent,
        TimeCourseLogDetailComponent,
        TimeCourseLogUpdateComponent,
        TimeCourseLogDeleteDialogComponent,
        TimeCourseLogDeletePopupComponent
    ],
    entryComponents: [
        TimeCourseLogComponent,
        TimeCourseLogUpdateComponent,
        TimeCourseLogDeleteDialogComponent,
        TimeCourseLogDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebinarappTimeCourseLogModule {}
