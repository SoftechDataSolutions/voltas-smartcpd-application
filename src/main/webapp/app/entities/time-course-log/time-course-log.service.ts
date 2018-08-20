import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITimeCourseLog } from 'app/shared/model/time-course-log.model';

type EntityResponseType = HttpResponse<ITimeCourseLog>;
type EntityArrayResponseType = HttpResponse<ITimeCourseLog[]>;

@Injectable({ providedIn: 'root' })
export class TimeCourseLogService {
    private resourceUrl = SERVER_API_URL + 'api/time-course-logs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/time-course-logs';

    constructor(private http: HttpClient) {}

    create(timeCourseLog: ITimeCourseLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(timeCourseLog);
        return this.http
            .post<ITimeCourseLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(timeCourseLog: ITimeCourseLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(timeCourseLog);
        return this.http
            .put<ITimeCourseLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITimeCourseLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITimeCourseLog[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITimeCourseLog[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(timeCourseLog: ITimeCourseLog): ITimeCourseLog {
        const copy: ITimeCourseLog = Object.assign({}, timeCourseLog, {
            loggedin: timeCourseLog.loggedin != null && timeCourseLog.loggedin.isValid() ? timeCourseLog.loggedin.toJSON() : null,
            loggedout: timeCourseLog.loggedout != null && timeCourseLog.loggedout.isValid() ? timeCourseLog.loggedout.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.loggedin = res.body.loggedin != null ? moment(res.body.loggedin) : null;
        res.body.loggedout = res.body.loggedout != null ? moment(res.body.loggedout) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((timeCourseLog: ITimeCourseLog) => {
            timeCourseLog.loggedin = timeCourseLog.loggedin != null ? moment(timeCourseLog.loggedin) : null;
            timeCourseLog.loggedout = timeCourseLog.loggedout != null ? moment(timeCourseLog.loggedout) : null;
        });
        return res;
    }
}
