import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WebinarappSharedModule } from 'app/shared';
import {
    QuizHistoryComponent,
    QuizHistoryDetailComponent,
    QuizHistoryUpdateComponent,
    QuizHistoryDeletePopupComponent,
    QuizHistoryDeleteDialogComponent,
    quizHistoryRoute,
    quizHistoryPopupRoute
} from './';

const ENTITY_STATES = [...quizHistoryRoute, ...quizHistoryPopupRoute];

@NgModule({
    imports: [WebinarappSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        QuizHistoryComponent,
        QuizHistoryDetailComponent,
        QuizHistoryUpdateComponent,
        QuizHistoryDeleteDialogComponent,
        QuizHistoryDeletePopupComponent
    ],
    entryComponents: [QuizHistoryComponent, QuizHistoryUpdateComponent, QuizHistoryDeleteDialogComponent, QuizHistoryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebinarappQuizHistoryModule {}
