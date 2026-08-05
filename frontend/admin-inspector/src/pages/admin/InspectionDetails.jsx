import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { formatDate } from "../../utils/dateFormatter";

import {
    getInspectionDetails,
    getAllInspectors,
    assignInspector,

    reviewInspection,

    approveInspection,

    rejectInspection,

    sendForReInspection
} from "../../services/inspectionService";


const InspectionDetails = () => {


    const { inspectionId } = useParams();


    const [inspection, setInspection] = useState(null);
    
    const [testResults, setTestResults] = useState([]);
    const [inspectors, setInspectors] = useState([]);

    const [selectedInspector, setSelectedInspector] = useState("");



    useEffect(() => {

        loadInspection();

        loadInspectors();

    }, []);



const loadInspection = async () => {

    try {

        const response = await getInspectionDetails(inspectionId);

        console.log(response);

        setInspection(response.inspection);

        setTestResults(response.testResults);

    } catch (error) {

        console.log(error);

    }

};



    const loadInspectors = async () => {

        try {

            const response = await getAllInspectors();

            console.log("Inspectors:", response);

            setInspectors(response);

        }
        catch(error){

            console.log(error);

        }

    };



    const handleAssign = async () => {


        if(!selectedInspector){

            alert("Please select inspector");

            return;

        }


        try{

            await assignInspector(
                inspectionId,
                selectedInspector
            );


            alert("Inspector Assigned Successfully");


            loadInspection();


        }
        catch(error){

            console.log(error);

        }

    };

    const handleReview = async () => {

    try {

        await reviewInspection(inspectionId);

        alert("Inspection moved for Admin Review");

        loadInspection();

    }

    catch (error) {

        console.log(error);

    }

};

const handleApprove = async () => {

    try {

        await approveInspection(inspectionId);

        alert("Inspection Approved");

        loadInspection();

    }

    catch (error) {

        console.log(error);

    }

};

const handleReject = async () => {

    const reason = prompt("Enter rejection reason");

    if (!reason) return;

    try {

        await rejectInspection(
            inspectionId,
            reason
        );

        alert("Inspection Rejected");

        loadInspection();

    }

    catch (error) {

        console.log(error);

    }

};

const handleReInspection = async () => {

    const reason = prompt("Reason for Reinspection");

    if (!reason) return;

    try {

        await sendForReInspection(
            inspectionId,
            reason
        );

        alert("Sent for Reinspection");

        loadInspection();

    }

    catch (error) {

        console.log(error);

    }

};



    if(!inspection){

        return (

            <div className="p-6">

                Loading...

            </div>

        );

    }



    return (

       <div className="p-6">

    <h1 className="text-2xl font-bold mb-6">
        Inspection Details
    </h1>

    <div className="bg-white shadow rounded-lg p-6 space-y-3">

        <p><b>Inspection ID:</b> {inspection.inspectionId}</p>

        <p><b>Shop:</b> {inspection.shopName}</p>

        <p><b>Vendor:</b> {inspection.vendorName}</p>

        <p><b>Inspection Date:</b> {formatDate(inspection.inspectionDate)}</p>

        <p><b>Status:</b> {inspection.status}</p>

        <p>
            <b>Inspector:</b>{" "}
            {inspection.inspectorName || "Not Assigned"}
        </p>

        <p>
            <b>Inspection Score:</b>{" "}
            {inspection.overallInspectionScore || "Not Available"}
        </p>

        <p>
            <b>Recommendation:</b>{" "}
            {inspection.recommendation || "Not Available"}
        </p>

    </div>

    {/* WAITING */}

    {inspection.status === "SCHEDULED" && (

        <div className="mt-8">

            <h2 className="text-lg font-semibold mb-3">
                Assign Inspector
            </h2>

            <select

                className="border p-2 rounded"

                value={selectedInspector}

                onChange={(e) =>
                    setSelectedInspector(e.target.value)
                }

            >

                <option value="">
                    Select Inspector
                </option>

                {inspectors.map(inspector => (

                    <option

                        key={inspector.inspectorId}

                        value={inspector.inspectorId}

                    >

                        {inspector.firstName} {inspector.lastName}

                    </option>

                ))}

            </select>

            <button

                onClick={handleAssign}

                className="ml-3 bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded"

            >

                Assign Inspector

            </button>

        </div>

    )}

    {/* REPORT SUBMITTED */}

    {inspection.status === "REPORT_SUBMITTED" && (

        <div className="mt-8">

            <button

                onClick={handleReview}

                className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded"

            >

                Start Admin Review

            </button>

        </div>

    )}

    {/* UNDER ADMIN REVIEW */}

    {inspection.status === "UNDER_ADMIN_REVIEW" && (

        <div className="mt-8 flex gap-4">

            <button

                onClick={handleApprove}

                className="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded"

            >

                Approve

            </button>

            <button

                onClick={handleReject}

                className="bg-red-600 hover:bg-red-700 text-white px-5 py-2 rounded"

            >

                Reject

            </button>

            <button

                onClick={handleReInspection}

                className="bg-yellow-500 hover:bg-yellow-600 text-white px-5 py-2 rounded"

            >

                Send For Reinspection

            </button>

        </div>

    )}


    {/* Test Results */}

<div className="bg-white shadow rounded-lg p-6 mt-8">

    <h2 className="text-xl font-bold mb-5">
        Inspector Test Results
    </h2>

    {
        testResults.length === 0 ?

        <p className="text-gray-500">
            No Test Results Available
        </p>

        :

        testResults.map(result => (

            <div
                key={result.resultId}
                className="border rounded-lg p-5 mb-5 bg-gray-50"
            >

                <div className="flex justify-between items-center">

                    <h3 className="text-lg font-bold">
                        {result.productName}
                    </h3>

                    <span
                        className={`px-3 py-1 rounded text-white text-sm ${
                            result.outcome === "PURE"
                                ? "bg-green-600"
                                : result.outcome === "ADULTERATED"
                                ? "bg-red-600"
                                : "bg-yellow-500"
                        }`}
                    >
                        {result.outcome}
                    </span>

                </div>

                <div className="grid grid-cols-2 gap-4 mt-4">

                    <p>
                        <b>Test :</b>
                        {" "}
                        {result.testTitle}
                    </p>

                    <p>
                        <b>Adulterant :</b>
                        {" "}
                        {result.adulterantName}
                    </p>

                    <p>
                        <b>Sample Type :</b>
                        {" "}
                        {result.sampleType}
                    </p>

                    <p>
                        <b>Quantity :</b>
                        {" "}
                        {result.quantitySampleTaken}
                    </p>

                    <p>
                        <b>Score :</b>
                        {" "}
                        {result.scoreAwarded}
                    </p>

                    <p>
                        <b>Action Taken :</b>
                        {" "}
                        {result.actionTaken}
                    </p>

                    <p className="col-span-2">
                        <b>Sample Description :</b>
                        {" "}
                        {result.sampleDescription}
                    </p>

                    <p className="col-span-2">
                        <b>Observation :</b>
                        {" "}
                        {result.observationNotes}
                    </p>

                    {
                        result.labReferenceNo &&

                        <p className="col-span-2">
                            <b>Lab Reference No :</b>
                            {" "}
                            {result.labReferenceNo}
                        </p>

                    }

                    <p className="col-span-2 text-sm text-gray-500">
                        Tested At :
                        {" "}
                        {result.testedAt}
                    </p>

                </div>

            </div>

        ))

    }

</div>

</div>


    );


};


export default InspectionDetails;