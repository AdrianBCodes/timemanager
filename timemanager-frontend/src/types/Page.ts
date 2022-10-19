type Page<T> = {
    content: T[];
    pageable: any;
    last: boolean;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    sort: {
        empty: boolean,
        sorted: boolean,
        unsorted: boolean
    };
    numberOfElements: number;
    first: boolean;
    empty: boolean;
}
export default Page;