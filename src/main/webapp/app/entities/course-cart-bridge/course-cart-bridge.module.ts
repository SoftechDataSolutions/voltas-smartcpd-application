import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebinarappSharedModule } from 'app/shared';
import {
    CourseCartBridgeComponent,
    CourseCartBridgeDetailComponent,
    CourseCartBridgeUpdateComponent,
    CourseCartBridgeDeletePopupComponent,
    CourseCartBridgeDeleteDialogComponent,
    courseCartBridgeRoute,
    courseCartBridgePopupRoute
} from './';

const ENTITY_STATES = [...courseCartBridgeRoute, ...courseCartBridgePopupRoute];

@NgModule({
    imports: [WebinarappSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CourseCartBridgeComponent,
        CourseCartBridgeDetailComponent,
        CourseCartBridgeUpdateComponent,
        CourseCartBridgeDeleteDialogComponent,
        CourseCartBridgeDeletePopupComponent
    ],
    entryComponents: [
        CourseCartBridgeComponent,
        CourseCartBridgeUpdateComponent,
        CourseCartBridgeDeleteDialogComponent,
        CourseCartBridgeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebinarappCourseCartBridgeModule {}
