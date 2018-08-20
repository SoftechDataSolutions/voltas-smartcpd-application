import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WebinarappCustomerModule } from './customer/customer.module';
import { WebinarappCompanyModule } from './company/company.module';
import { WebinarappTopicModule } from './topic/topic.module';
import { WebinarappCourseModule } from './course/course.module';
import { WebinarappCourseCartBridgeModule } from './course-cart-bridge/course-cart-bridge.module';
import { WebinarappCertificateModule } from './certificate/certificate.module';
import { WebinarappSectionModule } from './section/section.module';
import { WebinarappQuizModule } from './quiz/quiz.module';
import { WebinarappQuestionModule } from './question/question.module';
import { WebinarappChoiceModule } from './choice/choice.module';
import { WebinarappCourseHistoryModule } from './course-history/course-history.module';
import { WebinarappQuestionHistoryModule } from './question-history/question-history.module';
import { WebinarappSectionHistoryModule } from './section-history/section-history.module';
import { WebinarappQuizHistoryModule } from './quiz-history/quiz-history.module';
import { WebinarappOrdersModule } from './orders/orders.module';
import { WebinarappCartModule } from './cart/cart.module';
import { WebinarappTimeCourseLogModule } from './time-course-log/time-course-log.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        WebinarappCustomerModule,
        WebinarappCompanyModule,
        WebinarappTopicModule,
        WebinarappCourseModule,
        WebinarappCourseCartBridgeModule,
        WebinarappCertificateModule,
        WebinarappSectionModule,
        WebinarappQuizModule,
        WebinarappQuestionModule,
        WebinarappChoiceModule,
        WebinarappCourseHistoryModule,
        WebinarappQuestionHistoryModule,
        WebinarappSectionHistoryModule,
        WebinarappQuizHistoryModule,
        WebinarappOrdersModule,
        WebinarappCartModule,
        WebinarappTimeCourseLogModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WebinarappEntityModule {}
