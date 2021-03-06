import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICertificate } from 'app/shared/model/certificate.model';

type EntityResponseType = HttpResponse<ICertificate>;
type EntityArrayResponseType = HttpResponse<ICertificate[]>;

@Injectable({ providedIn: 'root' })
export class CertificateService {
    private resourceUrl = SERVER_API_URL + 'api/certificates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/certificates';

    constructor(private http: HttpClient) {}

    create(certificate: ICertificate): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(certificate);
        return this.http
            .post<ICertificate>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(certificate: ICertificate): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(certificate);
        return this.http
            .put<ICertificate>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICertificate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICertificate[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICertificate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(certificate: ICertificate): ICertificate {
        const copy: ICertificate = Object.assign({}, certificate, {
            timestamp: certificate.timestamp != null && certificate.timestamp.isValid() ? certificate.timestamp.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.timestamp = res.body.timestamp != null ? moment(res.body.timestamp) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((certificate: ICertificate) => {
            certificate.timestamp = certificate.timestamp != null ? moment(certificate.timestamp) : null;
        });
        return res;
    }
}
