import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebinarappSharedModule } from 'app/shared';
import {
    QuestionComponent,
    QuestionDetailComponent,
    QuestionUpdateComponent,
    QuestionDeletePopupComponent,
    QuestionDeleteDialogComponent,
    questionRoute,
    questionPopupRoute
} from './';

const ENTITY_STATES = [...questionRoute, ...questionPopupRoute];

@NgModule({
    imports: [WebinarappSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        QuestionComponent,
        QuestionDetailComponent,
        QuestionUpdateComponent,
        QuestionDeleteDialogComponent,
        QuestionDeletePopupComponent
    ],
    entryComponents: [QuestionComponent, QuestionUpdateComponent, QuestionDeleteDialogComponent, QuestionDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebinarappQuestionModule {}
