import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISectionHistory } from 'app/shared/model/section-history.model';

type EntityResponseType = HttpResponse<ISectionHistory>;
type EntityArrayResponseType = HttpResponse<ISectionHistory[]>;

@Injectable({ providedIn: 'root' })
export class SectionHistoryService {
    private resourceUrl = SERVER_API_URL + 'api/section-histories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/section-histories';

    constructor(private http: HttpClient) {}

    create(sectionHistory: ISectionHistory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sectionHistory);
        return this.http
            .post<ISectionHistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sectionHistory: ISectionHistory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sectionHistory);
        return this.http
            .put<ISectionHistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISectionHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISectionHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISectionHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(sectionHistory: ISectionHistory): ISectionHistory {
        const copy: ISectionHistory = Object.assign({}, sectionHistory, {
            startdate: sectionHistory.startdate != null && sectionHistory.startdate.isValid() ? sectionHistory.startdate.toJSON() : null,
            lastactivedate:
                sectionHistory.lastactivedate != null && sectionHistory.lastactivedate.isValid()
                    ? sectionHistory.lastactivedate.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startdate = res.body.startdate != null ? moment(res.body.startdate) : null;
        res.body.lastactivedate = res.body.lastactivedate != null ? moment(res.body.lastactivedate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sectionHistory: ISectionHistory) => {
            sectionHistory.startdate = sectionHistory.startdate != null ? moment(sectionHistory.startdate) : null;
            sectionHistory.lastactivedate = sectionHistory.lastactivedate != null ? moment(sectionHistory.lastactivedate) : null;
        });
        return res;
    }
}
