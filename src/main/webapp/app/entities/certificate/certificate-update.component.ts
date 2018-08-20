import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ICertificate } from 'app/shared/model/certificate.model';
import { CertificateService } from './certificate.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
    selector: 'jhi-certificate-update',
    templateUrl: './certificate-update.component.html'
})
export class CertificateUpdateComponent implements OnInit {
    private _certificate: ICertificate;
    isSaving: boolean;

    customers: ICustomer[];
    timestamp: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private certificateService: CertificateService,
        private customerService: CustomerService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ certificate }) => {
            this.certificate = certificate;
        });
        this.customerService.query().subscribe(
            (res: HttpResponse<ICustomer[]>) => {
                this.customers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.certificate.timestamp = moment(this.timestamp, DATE_TIME_FORMAT);
        if (this.certificate.id !== undefined) {
            this.subscribeToSaveResponse(this.certificateService.update(this.certificate));
        } else {
            this.subscribeToSaveResponse(this.certificateService.create(this.certificate));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICertificate>>) {
        result.subscribe((res: HttpResponse<ICertificate>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }
    get certificate() {
        return this._certificate;
    }

    set certificate(certificate: ICertificate) {
        this._certificate = certificate;
        this.timestamp = moment(certificate.timestamp).format(DATE_TIME_FORMAT);
    }
}
